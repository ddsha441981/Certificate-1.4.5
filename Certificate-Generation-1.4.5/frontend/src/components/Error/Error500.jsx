'use client'

import { useState, useEffect } from 'react'
import { motion } from 'framer-motion'
import Link from 'next/link'
import { Button } from "@/components/ui/button"
import { Rocket, Circle, Star } from 'lucide-react'

 function Error500() {
  const [stars, setStars] = useState([])

  useEffect(() => {
    const generateStars = () => {
      const newStars = []
      for (let i = 0; i < 50; i++) {
        newStars.push({
          id: i,
          x: Math.random() * 100,
          y: Math.random() * 100,
          size: Math.random() * 2 + 1,
        })
      }
      setStars(newStars)
    }

    generateStars()
  }, [])

  return (
    <div className="min-h-screen bg-gray-900 flex flex-col items-center justify-center overflow-hidden relative">
      {stars.map((star) => (
        <motion.div
          key={star.id}
          className="absolute bg-white rounded-full"
          style={{
            left: `${star.x}%`,
            top: `${star.y}%`,
            width: `${star.size}px`,
            height: `${star.size}px`,
          }}
          animate={{
            opacity: [0, 1, 0],
            scale: [1, 1.2, 1],
          }}
          transition={{
            duration: Math.random() * 3 + 2,
            repeat: Infinity,
            repeatType: 'reverse',
          }}
        />
      ))}

      <motion.div
        className="text-9xl font-bold text-white mb-8"
        initial={{ opacity: 0, y: -50 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
      >
        500
      </motion.div>

      <motion.h1
        className="text-4xl font-semibold text-white mb-4"
        initial={{ opacity: 0, y: 50 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5, delay: 0.2 }}
      >
       - Internal Server Error
      </motion.h1>

      <motion.p
        className="text-xl text-gray-300 mb-8 text-center max-w-md"
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ duration: 0.5, delay: 0.4 }}
      >
       Oops! Something went wrong on our end. We're working to fix the issue.
      </motion.p>

      <motion.div
        initial={{ opacity: 0, scale: 0.5 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.5, delay: 0.6 }}
      >
        <Link href="/">
          <Button className="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded-full transition-all duration-300 transform hover:scale-105">
            <Rocket className="mr-2 h-4 w-4" /> Refresh Page
          </Button>
        </Link>
      </motion.div>


      <motion.div
            className="mt-8 text-sm text-gray-500" variants={childVariants} whileHover={{ scale: 1.05 }} >
                If the problem persists, please contact our support team.

      </motion.div>

      <motion.div
        className="absolute bottom-10 left-10"
        animate={{
          y: [0, -20, 0],
        }}
        transition={{
          duration: 5,
          repeat: Infinity,
          repeatType: 'reverse',
        }}
      >
        <div className="relative">
          <Circle className="text-blue-300 h-24 w-24" />
          <Circle className="text-blue-200 h-16 w-16 absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2" />
          <Circle className="text-blue-100 h-8 w-8 absolute top-1/4 left-1/4" />
        </div>
      </motion.div>

      <motion.div
        className="absolute top-10 right-10"
        animate={{
          rotate: 360,
        }}
        transition={{
          duration: 20,
          repeat: Infinity,
          ease: 'linear',
        }}
      >
        <Star className="text-yellow-300 h-16 w-16" />
      </motion.div>

      <motion.div
        className="absolute"
        animate={{
          x: [-1000, 1000],
          y: [0, -100, 0],
        }}
        transition={{
          duration: 20,
          repeat: Infinity,
          repeatType: 'loop',
        }}
      >
        <Rocket className="text-red-500 h-12 w-12" />
      </motion.div>
    </div>
  )
}
export default Error500;